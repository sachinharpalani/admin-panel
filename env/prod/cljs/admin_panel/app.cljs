(ns admin-panel.app
  (:require [admin-panel.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
