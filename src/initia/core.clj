(ns initia.core
  (:gen-class)
  (:require
    [clojure.pprint :refer [pprint]]
    [clojure.string :as str]
    [clojure.tools.cli :refer [parse-opts]]
    [initia.text-metric :as metric]))


(def available-metrics
  "Available similarity/distance metrics"
  {:ngram-sim         ["N-Gram similarity" metric/ngram-sim]
   :lcs-dist          ["LCS distance" metric/lcs-dist]
   :lcs-sim           ["LCS similarity" metric/lcs-sim]
   :cosine-dist       ["Cosine distance" metric/cosine-dist]
   :cosine-sim        ["Cosine similarity" metric/cosine-sim]
   :jaccard-dist      ["Jaccard distance" metric/jaccard-dist]
   :jaccard-sim       ["Jaccard similarity" metric/jaccard-sim]
   :jaro-winkler-dist ["Jaro-Winkler distance" metric/jaro-winkler-dist]
   :jaro-winkler-sim  ["Jaro-Winkler similarity" metric/jaro-winkler-sim]
   :levenshtein-dist  ["Levenshtein distance" metric/levenshtein-dist]
   :levenshtein-sim   ["Levenshtein similarity" metric/levenshtein-sim]
   :damerau-dist      ["Damerau distance" metric/damerau-dist]
   :damerau-sim       ["Damerau similarity" metric/damerau-sim]
   :medieval-sim      ["Medieval similarity (weighted levenshtein)" metric/medieval-sim]
   :medieval-dist     ["Medieval distance (weighted levenshtein)" metric/medieval-dist]})


(defn get-metric
  "Get metric for keyword"
  [metric-key]
  (if-let [[_ metric-fn] (get available-metrics metric-key)]
    metric-fn
    (throw (ex-info "Unknown metric" {:metric metric-key
                                      :available (keys available-metrics)}))))


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
        (str/join "\n" (for [[k [metric-name _]] available-metrics]
                         (str "  " (name k) " - " metric-name)))]
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


(defn run-analysis
  "Run the analysis of the initia"
  [options]
  (try
    (when (:verbose options)
      (println "Verbose"))
    (pprint options)
    (System/exit 0)
    (catch Exception e
      (println (str "Error: " (.getMessage e)))
      (when (:verbose options)
        (.printStackTrace e))
      (System/exit 1))))


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
