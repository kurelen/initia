(ns initia.data-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [initia.data :as data]))


(deftest test-fixtures
  (testing "testing dataset 1"
    (is (= 50 (count data/testset-1))))

  (testing "testing dataset 2"
    (is (= 100 (count data/testset-2)))))
