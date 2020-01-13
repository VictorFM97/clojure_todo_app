(ns todo-app.tasks-repository)

(defrecord Task [id profile-id title description creation-date])
(def tasks (atom []))

(defn get-last-id
  []
  (let [id (:id (last @tasks))]
    (if (nil? id)
      1
      (inc id))))

(defn add!
  [task]
  (let [{profile-id :profile-id title :title description :description} task
        date (java.util.Date.)
        id (get-last-id)]
    (swap! tasks conj (->Task id profile-id title description date))
    (last @tasks)))

(defn remove!
  [id]
  nil)

(defn order-by
  [func]
  (filter func @tasks))

(defn get-all []
  @tasks)