package main

import (
	"fmt"
	"math/rand"
	"time"
)

// -1 - table is empty
// 0 - not enough tobacco
// 1 - not enough paper
// 2 - not enough matches
var not_enough int = -1
var smphore = make(chan bool, 1)
var done = make(chan bool, 1)
var inter_done = make(chan bool, 1)

func smoker_with(item int, smoker_type string) {
	for {
		smphore <- true
		if not_enough == item {
			time.Sleep(100 * time.Millisecond)
			not_enough = -1
			fmt.Printf("Smoker with %s stop smoking\n", smoker_type)

			done <- true
		}
		<-smphore
	}
}

func interm() {
	requests_count := 10

	for i := 0; i < requests_count; i++ {
		smphore <- true
		not_enough = rand.Intn(3)
		<-smphore

		<-done
	}

	inter_done <- true
}

func main() {
	go smoker_with(0, "tobacco")
	go smoker_with(1, "paper")
	go smoker_with(2, "matches")

	go interm()

	<-inter_done
}
