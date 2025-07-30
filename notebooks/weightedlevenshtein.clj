;; # Gewichteter Levenshtein
;;
;; Spezielle Substitutionskosten, um mittelalterliche Initien besser vergleichen zu können

(ns notebooks.weightedlevenshtein
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [initia.data :as data]
    [initia.matrix :as matrix]
    [initia.text-metric :as metric]
    [initia.visual :as visual]
    [nextjournal.clerk :as clerk]))


;; Um die Substitutionskosten in der gewichteten Levenshtein Ähnlichkeit
;; auf mittelalterliche deutsche Texte anzupassen, haben wir eine
;; Substitutionskosten Tabelle erstellt:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(clerk/table
  {::clerk/page-size nil}
  {:head ["Erster Buchstabe" "Zweiter Buchstabe" "Kosten"]
   :rows (->> data/weights
              :substitute
              seq
              (mapv (fn [[[a b] c]] [a b c])))})


;; Zum Substituieren schauen wir in dieser Tabelle nach, ob die Buchstaben
;; kleingeschreiben in dieser oder umgekehrter Reihenfolge auftauchen,
;; ansonsten gibt es die Standardkosten von 1.

^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def initien-1 (map :initium data/testset-1))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def initien-2 (map :initium data/testset-2))


;; ## Anwendung auf die beiden Testsets
;;
;; ### Testset 1
;;
;; Das Testset 1 mit generierten Daten zeigt folgende Hitzekarte:

^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def medieval-matrix (matrix/symmetric metric/medieval-sim initien-1))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def levenshtein-matrix (matrix/symmetric metric/levenshtein-sim initien-1))


^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  medieval-matrix
  initien-1)


;; ### Testset 2
;;
;; Das Testset 2 mit Altdaten zeigt folgende Hitzekarte:

^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(visual/matrix-heatmap
  (matrix/symmetric metric/medieval-sim initien-2)
  initien-2
  :max-size 100)


;; ## Inhaltliche Analyse der Kategorien von Testset 1
;; 

^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def gruppen-indices
  (let [groups (->> data/testset-1
                    (map-indexed vector)
                    (group-by (comp (juxt :gruppe :kategorie) second))
                    (mapv (fn [[key indices]]
                            {:gruppe-kategorie key
                             :indices (mapv first indices)})))

        ausgangsform-lookup (->> groups
                                 (filter #(= "Ausgangsform" (second (:gruppe-kategorie %))))
                                 (into {} (map #(vector (first (:gruppe-kategorie %))
                                                        (first (:indices %))))))]
    (->> groups
         (remove #(or (= "Ausgangsform" (second (:gruppe-kategorie %)))
                      (= 1 (count (:indices %)))))
         (mapv #(cond-> %
                  (contains? ausgangsform-lookup (first (:gruppe-kategorie %)))
                  (update :indices conj (get ausgangsform-lookup (first (:gruppe-kategorie %)))))))))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def medieval-submatrices
  (mapv
    (fn [{:keys [gruppe-kategorie indices]}]
      {:gruppe-kategorie gruppe-kategorie
       :submatrix (matrix/extract-submatrix medieval-matrix indices)
       :initien (matrix/extract-subvector initien-1 indices)})
    gruppen-indices))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def levenshtein-submatrices
  (mapv
    (fn [{:keys [gruppe-kategorie indices]}]
      {:gruppe-kategorie gruppe-kategorie
       :submatrix (matrix/extract-submatrix levenshtein-matrix indices)
       :initien (matrix/extract-subvector initien-1 indices)})
    gruppen-indices))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(defn gruppe-kategorie-label
  [gruppe-kategorie]
  (str "Gruppe: " (first gruppe-kategorie) ", Kategorie: " (second gruppe-kategorie)))


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(defn caption
  [text]
  (clerk/html [:figcaption.text-center.mt-1 text]))


^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(for [idx (range (count medieval-submatrices))]
  [(clerk/html [:h3 (gruppe-kategorie-label (:gruppe-kategorie (nth medieval-submatrices idx)))])
   (clerk/row
     (let [{:keys [submatrix initien]} (nth medieval-submatrices idx)]
       (clerk/col
         (visual/matrix-heatmap submatrix initien :width 100)
         (caption "Gewichtete Levenshtein Ähnlichkeit")))
     (let [{:keys [submatrix initien]} (nth levenshtein-submatrices idx)]
       (clerk/col
         (visual/matrix-heatmap submatrix initien :width 100)
         (caption "Levenshtein Ähnlichkeit"))))])


;; ### Statistiken
;;
;; Für die Submatritzen mit der gewichteten Levenshtein Ähnlichkeit haben wir folgende
;; Werte:

(visual/matrix-stats-table
  (mapv :submatrix medieval-submatrices)
  (mapv #(gruppe-kategorie-label (:gruppe-kategorie %)) medieval-submatrices))


;; Für die Submatritzen mit der Levenshtein Ähnlichkeit haben wir folgende
;; Werte:

(visual/matrix-stats-table
  (mapv :submatrix levenshtein-submatrices)
  (mapv #(gruppe-kategorie-label (:gruppe-kategorie %)) medieval-submatrices))
