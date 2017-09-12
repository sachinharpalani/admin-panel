(ns admin-panel.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [admin-panel.layout :refer [error-page]]
            [admin-panel.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [admin-panel.env :refer [defaults]]
            [mount.core :as mount]
            [admin-panel.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
