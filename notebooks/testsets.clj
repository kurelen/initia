;; # Testsets
;; 
;; Für die Analyse stehen uns zwei Testsets an Initien zur Verfügung.
;; 
;; Das erste Testset besteht aus 11 Initien aus dem Altbestand sowie 
;; 39 Initien, welche mit bestimmten Mutationen aus den ersten Initien
;; generiert wurden. 
;; 
;; Das zweite Testset besteht aus 100 Initien aus dem Altbestand


(ns notebooks.testsets
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [initia.data :as data]
    [nextjournal.clerk :as clerk]))


;; ## Testset 1

^{::clerk/visibility {:result :show}}
(clerk/table
  {:head ["Index" "Initium"]
   :rows (map-indexed
           (fn [idx text] [idx text]) data/testset-1)})


;; ## Testset 2

^{::clerk/visibility {:result :show}}
(clerk/table
  {:head ["Index" "Initium"]
   :rows (map-indexed (fn [idx text] [idx text]) data/testset-2)})
