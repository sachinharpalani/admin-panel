(ns admin-panel.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[admin-panel started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[admin-panel has shut down successfully]=-"))
   :middleware identity})
