(ns initia.visual
  (:require
    [clojure.core.matrix :as m]
    [initia.matrix :as matrix]
    [nextjournal.clerk :as clerk]))


(defn matrix-heatmap
  "Visualise a symmetric matrix as a heat-map"
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


(defn matrix-stats-table
  "Display the stats of several matrizes"
  [matrices labels]
  (clerk/table
    (map
      (fn [m l]
        (let [stats (matrix/matrix-stats m)]
          {:Label l
           :Min (format "%.3f" (:min stats))
           :Max (format "%.3f" (:max stats))
           :Mittelwert (format "%.3f" (:mean stats))}))
      matrices
      labels)))
