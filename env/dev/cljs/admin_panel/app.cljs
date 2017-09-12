(ns ^:figwheel-no-load admin-panel.app
  (:require [admin-panel.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
