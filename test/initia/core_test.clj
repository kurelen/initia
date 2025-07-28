(ns initia.core-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [initia.data :as data]))


(deftest test-placeholder
  (testing "test testset 1"
    (is (= 50 (count data/testset-1))))

  (testing "test testset 2"
    (is (= 100 (count data/testset-2)))))
