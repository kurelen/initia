;; # Textmetriken
;;
;; [← Zurück zur Übersicht](index.html)
;;
;; Vergleich verschiedener String-Ähnlichkeitsmetriken für mittelalterliche Textanfänge.

(ns notebooks.textmetriken
  {:nextjournal.clerk/visibility {:code :hide}}
  (:require
    [initia.data :as data]
    [initia.matrix :as matrix]
    [initia.text-metric :as metric]
    [initia.visual :as visual]
    [nextjournal.clerk :as clerk]))


;; ## Mathematische Grundlagen

;; Eine **Metrik** (auch Abstandsfunktion) $d$ weist Elementen $x, y, z$ einer 
;; Menge $M$ einen nicht-negativen, reellen Wert zu mit den Eigenschaften:

;; **1. Positiv-definit:**
(clerk/tex "d(x, y) \\geq 0 \\quad \\text{und} \\quad d(x, y) = 0 \\Leftrightarrow x = y")


;; **2. Symmetrie:**
(clerk/tex "d(x, y) = d(y, x)")


;; **3. Dreiecksungleichung:**
(clerk/tex "d(x, z) \\leq d(x, y) + d(y, z)")


;; Für unsere Anwendung betrachten wir **Textmetriken**, wobei unsere Elemente 
;; Texte (Strings) sind. Sei $\Sigma^*$ die Menge aller Strings über einem 
;; Alphabet $\Sigma$, dann ist eine Textmetrik eine Funktion:

(clerk/tex "d: \\Sigma^* \\times \\Sigma^* \\rightarrow \\mathbb{R}^+_0")


;; ## Ähnlichkeitsfunktionen

;; Da sich Textmetriken in der Größe ihrer Ergebnisse stark unterscheiden können,
;; normieren wir diese zu **Ähnlichkeitsfunktionen** (Similarity). Eine 
;; Ähnlichkeitsfunktion $s$ transformiert eine Distanzmetrik $d$ so, dass:

(clerk/tex "s: \\Sigma^* \\times \\Sigma^* \\rightarrow [0, 1]")


;; mit den Eigenschaften:

;; **1. Normalisiert:**
(clerk/tex "s(x, y) \\in [0, 1] \\quad \\forall x, y \\in \\Sigma^*")


;; **2. Identität bedeutet maximale Ähnlichkeit:**
(clerk/tex "s(x, x) = 1 \\quad \\forall x \\in \\Sigma^*")


;; **3. Inverse Beziehung zur Distanz:**
(clerk/tex "s(x, y) = 1 \\Leftrightarrow d(x, y) = 0")


;; Eine häufig verwendete Normalisierung ist:

(clerk/tex "s(x, y) = 1 - \\frac{d(x, y)}{d_{\\text{max}}(x, y)}")


;; wobei $d_{\\text{max}}(x, y)$ die theoretisch maximale Distanz zwischen $x$ und $y$ 
;; bezeichnet (oft $\\max(|x|, |y|)$ bei editierbasierten Metriken).
;;
;; ## Visualisierung als Distanzmatrix

;; Wir wenden paarweise auf die Elemente unseres Testsets eine 
;; Ähnlichkeitsfunktionen an und visualisieren 
;; die Ergebnisse in einer **Hitzekarte** (Heatmap). 
;; 
;; In dieser Visualisierung gilt: **Je größer die Ähnlichkeit, umso heller**.

;; ## Analyse unterschiedlicher Ähnlichkeitsfunktionen
;; 
;; Im folgenden wenden wir das Testset 1 auf folgende Ähnlichkeitsfunktionen an:
;;
;; - N-Gram
;; - Longest common subsequence
;; - Cosine
;; - Jaccard
;; - Jaro-Winkler
;; - Levenshtein
;; - Damerau
;;
;; Die Ergebnisse zeigen wir als Hitzekarte an und geben anschließend Statistiken
;; zu den Ähnlichkeitsmatritzen.
;;

^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def label
  ["N-Gram"
   "Longest common subsequence"
   "Cosine"
   "Jaccard"
   "Jaro-Winkler"
   "Levenshtein"
   "Damerau"])


^{:nextjournal.clerk/visibility {:result :hide :code :hide}}
(def results
  (map #(matrix/symmetric % data/testset-1)
       [metric/ngram-sim
        metric/lcs-sim
        metric/cosine-sim
        metric/jaccard-sim
        metric/jaro-winkler-sim
        metric/levenshtein-sim
        metric/damerau-sim]))


^{:nextjournal.clerk/visibility {:result :show :code :hide}}
(map
  (fn [m l]
    [(clerk/html [:h3 l])
     (visual/matrix-heatmap m data/testset-1)])
  results
  label)


;; ### Statistiken
;;
;; Die Ähnlichkeitsmatritzen haben folgende Statistiken:
;; 
(visual/matrix-stats-table results label)


;; Dem gegenüber sieht man bei den entsprechenden Textmetriken höhere Abweichungen und
;; dementsprechend schlechtere Vergleichbarkeit

(visual/matrix-stats-table
  (map #(matrix/symmetric % data/testset-1)

       [metric/ngram-dist
        metric/lcs-dist
        metric/cosine-dist
        metric/jaccard-dist
        metric/jaro-winkler-dist
        metric/levenshtein-dist
        metric/damerau-dist])
  label)
