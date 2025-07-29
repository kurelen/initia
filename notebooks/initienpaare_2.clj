;; # Initienpaare auf Testset 1
;; 

(ns notebooks.initienpaare-2
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [initia.data :refer [testset-2]]
    [initia.text-metric :refer [medieval-sim]]
    [initia.visual :refer [pairwise-table]]))


;; ## Sortierte Ähnlichkeit mit Darstellung der Gruppen

(pairwise-table
  testset-2
  medieval-sim
  :gruppe
  :initium
  #(str "Gruppe " (:gruppe %)))
