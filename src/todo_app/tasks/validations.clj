(ns todo-app.tasks.validations)

(defn deleted?
  [task]
  (:deleted task))

(defn valid?
  [task]
  (let [{title :title
         description :description
         profile-id :profile-id} task]
    (and (string? title)
         (> (count title) 0)
         (string? description)
         (> (count description) 0)
         (number? profile-id)
         (> profile-id 0))))