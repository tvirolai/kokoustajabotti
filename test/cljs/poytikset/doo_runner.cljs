(ns poytikset.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [poytikset.core-test]))

(doo-tests 'poytikset.core-test)

