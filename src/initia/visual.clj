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


(defn- all-pairs
  "Generiert alle ungeordneten Paare aus einer Sammlung"
  [coll]
  (for [i (range (count coll))
        j (range (inc i) (count coll))]
    [(nth coll i) (nth coll j)]))


(defn- similarity-pairs
  "Berechnet Ähnlichkeit für alle Paare und sortiert nach Ähnlichkeit"
  [data sim-fn value-fn]
  (->> (all-pairs data)
       (map (fn [[entry1 entry2]]
              {:similarity (sim-fn (value-fn entry1) (value-fn entry2))
               :entry1 entry1
               :entry2 entry2}))
       (sort-by :similarity >)))


(defn group-color-rgb
  "RGB-basierte Farbgenerierung mit besserer Unterscheidbarkeit"
  [group-number]
  (let [n (dec group-number)  ; 0-50 Range
        ;; Golden Ratio für gleichmäßige Verteilung
        golden-ratio-conjugate 0.618033988749895
        hue (* (mod (* n golden-ratio-conjugate) 1.0) 360)
        ;; HSL zu RGB Konvertierung (vereinfacht für helle Pastellfarben)
        c (* 0.3 1.0)  ; Chroma (niedrig für Pastell)
        x (* c (- 1 (Math/abs (- (mod (/ hue 60) 2) 1))))
        m (- 0.9 (/ c 2))  ; Helligkeit anpassen
        [r' g' b'] (cond
                     (< hue 60)  [c x 0]
                     (< hue 120) [x c 0]
                     (< hue 180) [0 c x]
                     (< hue 240) [0 x c]
                     (< hue 300) [x 0 c]
                     :else       [c 0 x])
        r (Math/round (* (+ r' m) 255))
        g (Math/round (* (+ g' m) 255))
        b (Math/round (* (+ b' m) 255))]
    (format "rgb(%d, %d, %d)" r g b)))


(defn pairwise-table
  "Display pairwise comparison of text distance"
  [data sim-fn group-fn value-fn label-fn]
  (let [results (similarity-pairs data sim-fn value-fn)]
    (clerk/html
      [:div
       [:table {:style {:border-collapse "collapse" :width "100%" :font-size "14px"}}
        [:thead
         [:tr {:style {:background-color "#f8f9fa"}}
          [:th {:style {:border "1px solid #ddd" :padding "8px" :text-align "center" :width "10%"}} "Ähnlichkeit"]
          [:th {:style {:border "1px solid #ddd" :padding "8px" :width "45%"}} "Initium 1"]
          [:th {:style {:border "1px solid #ddd" :padding "8px" :width "45%"}} "Initium 2"]]]
        [:tbody
         (for [{:keys [similarity entry1 entry2]} results]
           (let [color1 (group-color-rgb (group-fn entry1))
                 color2 (group-color-rgb (group-fn entry2))]
             [:tr
              [:td {:style {:border "1px solid #ddd" :padding "8px" :text-align "center" :font-weight "bold"}}
               (format "%.3f" similarity)]
              [:td {:style {:border "1px solid #ddd" :padding "8px" :background-color color1}}
               [:div
                [:div {:style {:font-weight "bold" :margin-bottom "4px"}}
                 (label-fn entry1)]
                [:div {:style {:font-size "12px"}}
                 (value-fn entry1)]]]
              [:td {:style {:border "1px solid #ddd" :padding "8px" :background-color color2}}
               [:div
                [:div {:style {:font-weight "bold" :margin-bottom "4px"}}
                 (label-fn entry2)]
                [:div {:style {:font-size "12px"}}
                 (value-fn entry2)]]]]))]]])))
