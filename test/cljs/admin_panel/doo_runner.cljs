(ns admin-panel.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [admin-panel.core-test]))

(doo-tests 'admin-panel.core-test)

