(ns todo-app.tasks.repository)

(defrecord Task [id profile-id title description creation-date done deleted])
(def tasks (atom []))

(defn get-tasks []
  @tasks)

(defn add!
  [task]
  (let [{profile-id :profile-id title :title description :description} task
        id (inc (count @tasks))
        date (java.util.Date.)]
    (swap! tasks conj (->Task id profile-id title description date false false)))
  (last @tasks))

(defn update!
  [id key value]
  (swap! tasks update-in [(dec id)] assoc key value))

(defn clear! []
  (reset! tasks []))