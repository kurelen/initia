(ns initia.core
  (:gen-class)
  (:require
    [clojure.pprint :refer [pprint]]
    [clojure.string :as str]
    [clojure.tools.cli :refer [parse-opts]]
    [initia.text-metric :as metric]))


(def available-metrics
  "Available similarity/distance metrics"
  {:medieval-sim  ["Medieval similarity (weighted levenshtein)" metric/medieval-sim]
   :medieval-dist ["Medieval distance (weighted levenshtein)" metric/medieval-dist]})


(def cli-options
  [["-m" "--metric METRIC" "Similarity metric to use"
    :default :medieval-sim
    :parse-fn keyword
    :validate [#(contains? available-metrics %)
               (str "Must be one of\n  "
                    (str/join "\n  " (map name (keys available-metrics))))]]

   ["-i" "--input FILE" "Input file with initia"]

   ["-o" "--output FILE" "Output file for results"]

   ["-v" "--verbose" "Verbose output"]

   ["-h" "--help" "Show help"]])


(defn usage
  [options-summary]
  (->> ["Clustering tool for medieval initia"
        ""
        "Usage: clj -M:run [options]"
        "   or: java -jar initia.jar [options]"
        ""
        "Options:"
        options-summary
        ""
        "Available metrics:"
        "  NOT YET IMPLEMENTED"]
       (str/join \newline)))


(defn error-msg
  [errors]
  (str "The following errors occured while parsing your command:\n\n"
       (str/join \newline errors)))


(defn validate-args
  "Validate command line arguments"
  [args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}

      errors
      {:exit-message (error-msg errors) :ok? false}

      :else
      {:action :run :options options})))


(defn -main
  "Main entry point for the application."
  [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (do (println exit-message)
          (System/exit (if ok? 0 1)))
      (case action
        :run (pprint options)
        (do (println "Unknown action")
            (System/exit 1))))))
