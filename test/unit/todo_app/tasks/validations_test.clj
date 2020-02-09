(ns todo-app.tasks.validations-test
  (:require [midje.sweet :refer :all]
            [todo-app.tasks.validations :as v]))

(def task {:title "abc"
           :description "def"
           :profile-id 1
           :deleted false})

(facts "valid"
       (fact "Is task valid"
             (v/valid? task) => true)
       (fact "Is task invalid"
             (v/valid? {}) => false)
       (v/valid? (assoc task :title nil)) => false
       (v/valid? (assoc task :title "")) => false
       (v/valid? (assoc task :title 1)) => false
       (v/valid? (assoc task :description nil)) => false
       (v/valid? (assoc task :description "")) => false
       (v/valid? (assoc task :description 1)) => false
       (v/valid? (assoc task :profile-id "1")) => false
       (v/valid? (assoc task :profile-id nil)) => false
       (v/valid? (assoc task :profile-id 0)) => false)

(facts "deleted"
       (fact "Task isn't deleted"
             (v/deleted? task) => false)
       (fact "Task is deleted"
             (v/deleted? (assoc task :deleted true)) => true))