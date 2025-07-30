;; # Testsets
;; 
;; Für die Analyse stehen uns zwei Testsets an Initien zur Verfügung.
;; 


(ns notebooks.testsets
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [initia.data :as data]
    [initia.visual :as visual]
    [nextjournal.clerk :as clerk]))


;; ## Testset 1
;; 
;; Das erste Testset besteht aus 11 Initien aus dem Altbestand sowie 
;; 39 Initien, welche mit bestimmten Mutationen aus den ersten Initien
;; generiert wurden. 

^{::clerk/visibility {:result :show}}
(visual/data-table
  data/testset-1
  [:gruppe :kategorie :initium :herkunft])


;; ## Testset 2
;; 
;; Das zweite Testset besteht aus 100 Initien aus dem Altbestand des
;; Handschriftenportals.

^{::clerk/visibility {:result :show}}
(visual/data-table
  data/testset-2
  [:gruppe :initium :bemerkung])
