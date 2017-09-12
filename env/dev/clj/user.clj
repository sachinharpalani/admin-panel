(ns user
  (:require [mount.core :as mount]
            [admin-panel.figwheel :refer [start-fw stop-fw cljs]]
            admin-panel.core))

(defn start []
  (mount/start-without #'admin-panel.core/repl-server))

(defn stop []
  (mount/stop-except #'admin-panel.core/repl-server))

(defn restart []
  (stop)
  (start))


