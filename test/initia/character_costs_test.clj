(ns initia.character-costs-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [initia.character-costs :as costs]))


(deftest test-create-substitution-cost-fn
  (testing "Creating substitution cost function"
    (let [test-costs {["a" "o"] 0.3
                      ["b" "p"] 0
                      ["x" "y"] 0.5}
          cost-fn (costs/create-substitution-cost-fn test-costs)]

      ;; Test identical characters
      (is (= 0.0 (cost-fn "a" "a")))
      (is (= 0.0 (cost-fn "z" "z")))

      ;; Test defined substitutions
      (is (= 0.3 (cost-fn "a" "o")))
      (is (= 0.3 (cost-fn "o" "a"))) ; bidirectional
      (is (= 0.0 (cost-fn "b" "p")))
      (is (= 0.0 (cost-fn "p" "b"))) ; bidirectional
      (is (= 0.5 (cost-fn "x" "y")))

      ;; Test undefined substitutions (should default to 1.0)
      (is (= 1.0 (cost-fn "a" "z")))
      (is (= 1.0 (cost-fn "q" "w")))

      ;; Test case insensitivity
      (is (= 0.0 (cost-fn "a" "A")))
      (is (= 0.0 (cost-fn "z" "Z")))
      (is (= 0.3 (cost-fn "A" "O")))
      (is (= 0.3 (cost-fn "A" "o")))
      (is (= 0.3 (cost-fn "a" "O"))))))


(deftest test-cost-fn
  (testing "Pre-configured cost function for medieval scriptures"
    (is (map? costs/medieval-costs))
    (is (contains? costs/medieval-costs :substitute))
    (is (fn? (:substitute costs/medieval-costs)))

    (let [cost-fn (:substitute costs/medieval-costs)]
      ;; Test some known medieval character variations
      (is (= 0.0 (cost-fn "ä" "e"))) ; common medieval variation
      (is (= 0.0 (cost-fn "b" "p"))) ; common confusion in manuscripts
      (is (= 0.0 (cost-fn "f" "v"))) ; u/v variations in medieval texts
      (is (= 0.3 (cost-fn "c" "g"))) ; similar letterforms

      ;; Test identical characters
      (is (= 0.0 (cost-fn "a" "a")))

      ;; Test unrelated characters
      (is (= 1.0 (cost-fn "a" "z"))))))


(deftest test-edge-cases
  (testing "Edge cases for substitution cost function"
    (let [cost-fn (costs/create-substitution-cost-fn {["a" "b"] 0.5})]

      ;; Test empty strings (should be treated as characters)
      (is (number? (cost-fn "" "")))

      ;; Test single character strings
      (is (= 0.0 (cost-fn "a" "a")))

      ;; Test whitespace
      (is (= 0.0 (cost-fn " " " ")))
      (is (= 1.0 (cost-fn " " "a"))))))


(deftest test-cost-function-properties
  (testing "Properties of the cost function"
    (let [cost-fn (:substitute costs/medieval-costs)]

      ;; Symmetry: cost(a,b) = cost(b,a)
      (is (= (cost-fn "a" "o") (cost-fn "o" "a")))
      (is (= (cost-fn "b" "p") (cost-fn "p" "b")))

      ;; Identity: cost(a,a) = 0
      (doseq [char ["a" "b" "c" "x" "y" "z"]]
        (is (= 0.0 (cost-fn char char))))

      ;; Non-negativity: all costs >= 0
      (doseq [c1 ["a" "b" "c" "d" "e"]
              c2 ["a" "b" "c" "d" "e"]]
        (is (<= 0.0 (cost-fn c1 c2))))

      ;; Boundedness: all costs <= 1.0
      (doseq [c1 ["a" "b" "c" "d" "e"]
              c2 ["a" "b" "c" "d" "e"]]
        (is (<= (cost-fn c1 c2) 1.0))))))
