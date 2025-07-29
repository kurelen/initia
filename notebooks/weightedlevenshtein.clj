;; # Gewichteter Levenshtein
;;
;; Spezielle Substitutionskosten, um mittelalterliche Initien besser vergleichen zu können

(ns notebooks.weightedlevenshtein
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [clojure.core.matrix :as m]
    [initia.data :as data]
    [initia.matrix :as matrix]
    [initia.text-metric :as metric]
    [initia.visual :as visual]
    [nextjournal.clerk :as clerk]))


;; Um die Substitutionskosten in der gewichteten Levenshtein Ähnlichkeit
;; auf mittelalterliche deutsche und lateinische Texte anzupassen, haben
;; wir eine Substitutionskosten Tabelle erstellt:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(clerk/table
  {::clerk/page-size nil}
  {:head ["Erster Buchstabe" "Zweiter Buchstabe" "Kosten"]
   :rows (->> data/weights
              :substitute
              seq
              (mapv (fn [[[a b] c]] [a b c])))})


;; Zum substituieren schauen wir in dieser Tabelle noch, ob die Buchstaben
;; kleingeschreiben in dieser oder umgekehrter Reihenfolge auftauchen,
;; wenn nicht gibt es die Standardkosten von 1.

^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def initien-1 (map :initium data/testset-1))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def initien-2 (map :initium data/testset-2))


;; ## Anwendung auf die beiden Testsets
;;
;; ### Testset 1
;;
;; Das Testset 1 mit generierten Daten zeigt folgende Hitzekarte:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  (matrix/symmetric metric/medieval-sim initien-1)
  initien-1)


;; ### Testset 2
;;
;; Das Testset 2 mit Altdaten zeigt folgende Hitzekarte:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  (matrix/symmetric metric/medieval-sim initien-2)
  initien-2
  :max-size 100)


;; ## Differenz zwischen Levenshtein und gewichtetem Levenshtein
;;
;; Die von uns definierten Kosten machen alle Substitutionen günstiger. Es
;; ist aber nicht klar, ob dies dabei hilft, die einzelnen Gruppen besser
;; zu unterscheiden. Daher Visualisieren wir hier die Differenz zwischen den
;; Ähnlichkeitsmatrizen von Levenshtein zu dem gewichteten Levenshtein.
;;
;; ### Testset 1

^{:nextjournal.clerk/visibility {:result :hide :code :show}}
(def difference-1
  (m/sub (matrix/symmetric metric/medieval-sim initien-1)
         (matrix/symmetric metric/levenshtein-sim initien-1)))


;; Auf der absoluten Scala von $[0, 1]$ sieht die Differenz als Hitzekarte so aus: 

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  difference-1
  initien-1
  :domain [0 1])


;; Relativ sehen die Unterschiede aber so aus:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  difference-1
  initien-1)


;;
;; ### Testset 2

^{:nextjournal.clerk/visibility {:result :hide :code :show}}
(def difference-2
  (m/sub (matrix/symmetric metric/medieval-sim initien-2)
         (matrix/symmetric metric/levenshtein-sim initien-2)))


;; Auf der absoluten Scala von $[0, 1]$ sieht die Differenz als Hitzekarte so aus: 

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  difference-2
  initien-2
  :max-size 100
  :domain [0 1])


;; Relativ sehen die Unterschiede aber so aus:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  difference-2
  initien-2
  :max-size 100)


;; 
;; In beiden Fällen scheint es, als würde der gewichtete Levenshtein Algorithmus vor allem
;; die Elemente ähnlicher machen, welche nicht in der gleichen Gruppe liegen.
