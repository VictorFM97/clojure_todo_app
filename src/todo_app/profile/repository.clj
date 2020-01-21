(ns todo-app.profile.repository)

(defrecord Profile [id name creation-date])
(def profiles (atom []))

(defn order-by
  [pred]
  (filter pred @profiles))

(defn get-last-id
  []
  (let [id (:id (last @profiles))]
    (if (nil? id)
      1
      (inc id))))

(defn add!
  [profile]
  (let [{name :name} profile
        id (get-last-id)
        date (java.util.Date.)]
    (swap! profiles conj (->Profile id name date))))