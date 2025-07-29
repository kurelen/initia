(ns initia.data
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]))


(defn- load-edn-resource
  "Load resource file as vector of strings"
  [path]
  (with-open [r (io/reader (io/resource path))]
    (edn/read (java.io.PushbackReader. r))))


(def testset-1
  (load-edn-resource "fixtures/testset-1.edn"))


(def testset-2
  (load-edn-resource "fixtures/testset-2.edn"))
