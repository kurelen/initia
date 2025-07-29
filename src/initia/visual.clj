(ns initia.visual
  (:require
    [initia.matrix :as matrix]
    [clojure.core.matrix :as m]
    [nextjournal.clerk :as clerk]))


(defn matrix-heatmap
  "Erstellt eine Heatmap-Visualisierung einer Matrix."
  [matrix labels & {:keys [max-size width domain] :or {max-size 50 width 600}}]
  (let [n (min (m/row-count matrix) max-size)
        stats (matrix/matrix-stats matrix)
        domain (or domain [(:min stats) (:max stats)])

        data (for [i (range n)
                   j (range n)]
               {:x j
                :y i
                :value (m/mget matrix i j)
                :label-x (nth labels j)
                :label-y (nth labels i)})]
    (clerk/vl
      {:width width
       :height width
       :data {:values data}
       :mark "rect"
       :encoding {:x {:field "x"
                      :type "ordinal"
                      :axis {:title "Initium Index"
                             :labelAngle -45
                             :labelLimit 100}}
                  :y {:field "y"
                      :type "ordinal"
                      :axis {:title "Initium Index"}}
                  :color {:field "value"
                          :type "quantitative"
                          :scale {:scheme "magma"
                                  :domain domain}
                          :legend {:title "Ähnlichkeit"}}
                  :tooltip [{:field "label-x" :title "Initium X"}
                            {:field "label-y" :title "Initium Y"}
                            {:field "value" :title "Ähnlichkeit" :format ".3f"}]}})))
