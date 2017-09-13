(ns admin-panel.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [admin-panel.ajax :refer [load-interceptors!]]
            [admin-panel.events]
            [soda-ash.core :as sa])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  (let [selected-page (rf/subscribe [:page])]
    [:li.nav-item
     {:class (when (= page @selected-page) "active")}
     [:a.nav-link
      {:href uri
       :on-click #(reset! collapsed? true)} title]]))

(defn navbar []
  (r/with-let [collapsed? (r/atom true)]
    [:nav.navbar.navbar-dark.bg-primary
     [:button.navbar-toggler.hidden-sm-up
      {:on-click #(swap! collapsed? not)} "☰"]
     [:div.collapse.navbar-toggleable-xs
      (when-not @collapsed? {:class "in"})
      [:a.navbar-brand {:href "#/"} "admin-panel"]
      [:ul.nav.navbar-nav
       [nav-link "#/" "Home" :home collapsed?]
       [nav-link "#/about" "About" :about collapsed?]]]]))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:img {:src (str js/context "/img/warning_clojure.png")}]]]])


(defn on-submit [in]
  (js/alert (js->clj in)))

(defn get-prop-value [event data]
  (-> data
      (js->clj :keywordize-keys true)
      :value))

(defn get-file-object [data]
  (aget (.-files (.-target data)) 0))


(defn home-page []
  (let [state (r/atom {})
        add-prop (fn [key prop-value] (swap! state assoc key prop-value))
        add-img (fn [file] (swap! state assoc :image file))
        submit-props (fn [e] (js/alert @state))]
    (fn [props]
      [:div.container
       [sa/Form {:onSubmit #(submit-props %)}
        [sa/FormGroup {:widths "equal"}
         [sa/FormSelect {:label "Select a book :"
                         :placeholder "Books"
                         :options [{:text "HP1" :value "HP1"}
                                   {:text "HP2" :value "HP2"}]
                         :on-change #(add-prop :book (get-prop-value %1 %2))}]]
        [sa/FormInput {:label "Name :"
                       :placeholder "Please enter name of the stage"
                       :on-change #(add-prop :name (get-prop-value %1 %2))}]
        [sa/FormInput {:label "Description :"
                       :placeholder "Please enter description of the stage"
                       :on-change #(add-prop :desc (get-prop-value %1 %2))}]
        [sa/FormInput {:label "Outcome :"
                       :placeholder "Please enter outcome of the stage"
                       :on-change #(add-prop :outcome (get-prop-value %1 %2))}]
        [sa/Header {:size "small"} "Image"]
        [:input
         {:type "file"
          :accept "image/*"
          :label "Stage Image"
          :id "stage-image"
          :defaultValue ""
          :onChange #(add-img (aget (.-files (.-target %)) 0))}]
        [sa/Divider {:hidden true}]
        [sa/FormGroup {:inline true}
         [sa/FormButton {:content "Submit"
                         :positive true}]
         [sa/FormButton {:content "Reset"
                         :negative true}]]]])))

(def pages
  {:home #'home-page
   :about #'about-page})

(defn page []
  [:div
   [navbar]
   [(pages @(rf/subscribe [:page]))]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (rf/dispatch [:set-active-page :home]))

(secretary/defroute "/about" []
  (rf/dispatch [:set-active-page :about]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
