package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

func init() {
    rand.Seed(time.Now().Unix())
}

func check(e error) {
    if e != nil {
        panic(e)
    }
}
func sleep(dur int) {
    time.Sleep(time.Duration(rand.Intn(dur)) * time.Millisecond)
}

func gardener(garden [][]int, m *sync.RWMutex, wg *sync.WaitGroup) {
	N := len(garden)
	for step := 1;  step<=15; step++{
		sleep(3000)
		m.Lock()
		fmt.Println("Gardener started")
		for i := 0; i < N; i++ {
			for j := 0; j < N; j++ {
				if garden[i][j] != 5 {
					garden[i][j]++
				}
			}
		}
		fmt.Println("Gardener finished")
		fmt.Println()
		m.Unlock()
	}
	wg.Done()
}

func nature(garden [][]int, m *sync.RWMutex, wg *sync.WaitGroup) {
	N := len(garden)
	for step := 1;  step<=15; step++{
		sleep(5000)
		m.Lock()
		fmt.Println("Nature started")
		var destroy_num = rand.Intn(5)
		for i := 0; i < destroy_num; i++ {
			iPos := rand.Intn(N)
			jPos := rand.Intn(N)

			garden[iPos][jPos] = rand.Intn(5)
		}
		fmt.Println("Nature finished")
		fmt.Println()
		m.Unlock()
	}
	wg.Done()
}

func printOnScreen(garden [][]int, mutex *sync.RWMutex, wg *sync.WaitGroup) {
	N := len(garden)
	for step := 1;  step<=15; step++{
		sleep(2000)
		mutex.RLock()
		fmt.Println("PrintOnScreen started")
		for i := 0; i < N; i++ {
			for j := 0; j < N; j++ {
				fmt.Print(garden[i][j], " ")
			}
			fmt.Println()
		}
		fmt.Println("PrintOnScreen finished")
		fmt.Println();
		mutex.RUnlock()
	}
	wg.Done()
}

func printToFile(garden [][]int, mutex *sync.RWMutex, wg *sync.WaitGroup) {
	N := len(garden)
	for step := 1;  step<=15; step++{
		sleep(2000)
		mutex.RLock()
		fmt.Println("printToFile started")
		f, err := os.OpenFile("output.txt", os.O_APPEND|os.O_WRONLY, 0644)
		check(err)

		for i := 0; i < N; i++ {
			f.WriteString(fmt.Sprintln(garden[i]))
		}
		f.WriteString("\n");
		err = f.Close()
		check(err)
		fmt.Println("printToFile finished")
		fmt.Println();
		mutex.RUnlock()
	}
	wg.Done()
}

func viewer(mutex *sync.RWMutex, wg *sync.WaitGroup) {
	for step := 1;  step<=15; step++{
		sleep(3000)
		mutex.RLock()
		fmt.Println("Viewer started")
		sleep(2000)
		fmt.Println("Viewer finished")
		mutex.RUnlock()
	}
	wg.Done()
}

func main() {
	var sizeOfGarden = 5
	var garden = make([][]int, sizeOfGarden)

	var m sync.RWMutex

	for i := range garden {
		garden[i] = make([]int, sizeOfGarden)
		for j := range garden[i] {
			garden[i][j] = rand.Intn(5)
		}
	}

	f, err := os.Create("output.txt")
	check(err)
	err = f.Close()
	check(err)
	
	wg := sync.WaitGroup{}
	wg.Add(5)
	go gardener(garden, &m, &wg)
	go nature(garden, &m, &wg)
	go printOnScreen(garden, &m, &wg)
	go printToFile(garden, &m, &wg)
	go viewer(&m, &wg)

	wg.Wait()
}
