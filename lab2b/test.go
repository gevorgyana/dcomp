 package main

import (
  "fmt"
)

var list_of_tasks = []string{ "task_0", "task_1", "task_2", "task_3", "task_4", "task_5", "task_6", "task_7", "task_8", "task_9" }
var size = len(list_of_tasks)

var taken_away = make(chan string, size)
var load = make(chan string, size)

var doneflag = make(chan bool)

func first_worker() {
    for i := 0; i < size; i++ {
        taken_away <- list_of_tasks[i]
    }
}

func second_worker() {
    for i := 0; i < size; i++ {
        task := <-taken_away
        load <- task
    }
}

func third_worker() {
    for i := 0; i < size; i++ {
        task := <-load
        // calculating
        fmt.Println(task)
    }
    doneflag <- true
}

func main () {
    go first_worker()
    go second_worker()
    go third_worker()
    <-doneflag
}
