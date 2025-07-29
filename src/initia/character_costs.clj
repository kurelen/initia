(ns initia.character-costs
  (:require
    [clojure.string :refer [lower-case]]
    [initia.data :as data]))


(defn create-substitution-cost-fn
  "Creates a character substitution cost function from a cost lookup table."
  [cost-lookup]
  (fn [a b]
    (let [c (lower-case a)
          d (lower-case b)]
      (double (or (and (= c d) 0.0)
                  (cost-lookup [c d])
                  (cost-lookup [d c])
                  1.0)))))


(def medieval-costs
  "Pre-configured character substitution costs for medieval manuscripts."
  {:substitute
   (create-substitution-cost-fn
     (:substitute data/weights))})
