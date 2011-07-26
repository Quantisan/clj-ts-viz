(ns clj-ts-viz.model.kmeans
  (:use clj-ts-viz.model.process))


(defn- random-parition
  "Randomly assign a cluster for initialization"
  [coll n]
  (let [[cell & more]		coll
        rand-clus				(floor (rand n))]
    (while (seq cell)
      (cons (set-cluster rand-clus cell) (random-parition more n)))))

(defn- cluster-mean
  "Calculate the mean for a given cluster"
  [coll cluster]
  (let [subset	(filter #(= (:cluster %) cluster) coll)
        slopes	(get-slopes subset)]		; TODO: generalize

(defn kmeans-cluster
  "Takes a dataset and returns a new dataset with a new column of cluster label using quantile paritioning"
  [coll n]
  (let [slopes		(get-slopes coll)		; TODO: abstract out to generalize
        quantiles	(get-quantiles slopes n)]
    (map (partial set-quantile-partition quantiles) coll)))

;; testing only   
(require '[clj-ts-viz.data :as data])
(def data (data/gen-dataset))
(def xs (get-slopes data))