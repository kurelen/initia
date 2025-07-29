;; # Initienpaare auf Testset 1
;; 

(ns notebooks.initienpaare-1
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [initia.data :refer [testset-1]]
    [initia.text-metric :refer [medieval-sim]]
    [initia.visual :refer [pairwise-table]]))


;; ## Sortierte Ähnlichkeit mit Darstellung der Gruppen

(pairwise-table
  testset-1
  medieval-sim
  :label #(str "Gruppe " (:gruppe %) " • " (:kategorie %)))
