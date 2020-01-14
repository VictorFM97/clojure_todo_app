(ns todo-app.tasks.validations)

(defn is-deleted?
  [task]
  (:deleted task))