(ns initia.visual-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [initia.matrix :as matrix]
    [initia.visual :as visual]))


(deftest test-heatmap
  (testing "basic heatmap structure"
    (let [v ["a" "b" "c"]
          dist-fn (fn [x y] (if (= x y) 0 1))
          result (matrix/symmetric dist-fn v)
          heatmap (visual/matrix-heatmap result v)]
      (doseq [[k v] heatmap]
        (println k " " v))
      (is (map? heatmap))
      (is (contains? heatmap :nextjournal/value)))))
