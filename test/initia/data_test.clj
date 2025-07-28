(ns initia.data-test
  (:require
    [clojure.test :refer [deftest is testing]]))


(deftest test-fixtures
  (testing "testing dataset 1"
    (is (= true true)))

  (testing "testing dataset 2"
    (is (= true true))))
