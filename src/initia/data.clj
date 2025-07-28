(ns initia.data
  (:require
    [clojure.java.io :as io]))


(defn- load-resource-as-strings
  "Load resource file as vector of strings"
  [path]
  (with-open [r (io/reader (io/resource path))]
    (vec (line-seq r))))


(def testset-1
  (load-resource-as-strings "fixtures/testset-1.txt"))


(def testset-2
  (load-resource-as-strings "fixtures/testset-2.txt"))
