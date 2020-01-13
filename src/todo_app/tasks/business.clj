(ns todo-app.tasks-business
  (:require [todo-app.tasks-repository :as repo]))

(defn filter-tasks
  [pred]
  (repo/order-by pred))

(defn base-filter
  [id]
  #(= id (:profile-id %)))

(defn get-all
  [id]
  (filter-tasks (base-filter id)))

(defn get-most-recent
  [id]
  (last (filter-tasks (base-filter id))))

(defn add!
  [task]
  (repo/add! task))

(defn remove!
  [id]
  (repo/remove! id))