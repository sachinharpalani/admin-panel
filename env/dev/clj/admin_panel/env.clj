(ns admin-panel.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [admin-panel.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[admin-panel started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[admin-panel has shut down successfully]=-"))
   :middleware wrap-dev})
