(ns clj-ts-viz.data
  (:require [clojure.contrib.json :as json])
  (:require [incanter.stats :as stats]))

(def live-data (atom nil))

(def example-raw
  [{:id 0
    :balances
    [{:balance 100 :timestamp 1311638600}
     {:balance 150 :timestamp 1311638601}
     {:balance 200 :timestamp 1311638602}
     {:balance 150 :timestamp 1311638603}
     {:balance 180 :timestamp 1311638604}
     {:balance 160 :timestamp 1311638605}
     {:balance 200 :timestamp 1311638606}
     {:balance 210 :timestamp 1311638607}]
    :other-property 10}])

(def example-clustered
  [{:id 0
    :cluster 1
    :balances
    [{:balance 0 :timestamp 1311638600}
     {:balance 0 :timestamp 1311638601}
     {:balance 0 :timestamp 1311638602}
     {:balance 0 :timestamp 1311638603}
     {:balance 0 :timestamp 1311638604}
     {:balance 0 :timestamp 1311638605}
     {:balance 0 :timestamp 1311638606}
     {:balance 0 :timestamp 1311638607}]
    :other-property 10}])

(defn load-json [fname]
  (json/read-json (slurp fname)))

(defn jitter-timeline-piece [[timestamp balance]]
  [(+ (- (rand-int (* 2 86400)) 86400) timestamp)
   (* (+ 1.0 (- (rand 0.20) 0.05)) balance)])

(defn jitter-timeline [timeline]
  (sort-by first (map jitter-timeline-piece timeline)))

(defn jitter [record]
  "Jitters the data, just to make it un-recognizable."
  {:id             (hash (str (:uname record) "-salt:" (rand)))
   :fake-score     (+ (:score record) (- (rand-int 50) 25))
   :timeline       (jitter-timeline (:timeline record))})

(defn box-muller [mean variance]
  ""
  (let [x (- (* 2 (rand)) 1.0)
        y (- (* 2 (rand)) 1.0)
        r (+ (* x x) (* y y))]
    (if (or (> r 1) (== r 0))
      (box-muller mean variance)
      (let [k (* x (Math/sqrt (/ (* -2.0 (Math/log r)) r)))]
        (+ mean (* (Math/sqrt variance) x))))))

(defn gen-line [start length step-mean step-variance]
  (map vector
       (iterate inc 0)
       (map (partial + start)
            (reductions
             - (map (fn [_] (box-muller step-mean step-variance)) (range 0 length))))))

(defn gen-member [id balance-args]
  {:id id
   :balance (apply gen-line balance-args)})

(defn gen-dataset []
  (into (map (range 0 5) #(gen-line 1000 100 5 1))
        (map (range 0 5) #(gen-line 600 100 2 2))))
                
  
  