package main

import (
	"math/rand"
	"fmt"
	"time"
)

func fight(yin *int, yan *int) string {
    if *yin > *yan {
		time.Sleep(time.Millisecond * 100)
		*yin -= *yan
		*yan = 0
		fmt.Println("Guan-Yin wins")
		return "yin"
	} else {	
		time.Sleep(time.Millisecond * 100)
		*yan -= *yin
		*yin = 0
		fmt.Println("Guan-Yan wins")
		return "yan"
	}
}

func defeat(s []int, i int) []int {
    s[i] = s[len(s)-1]
    return s[:len(s)-1]
}

func main() {
	rand.Seed(time.Now().UnixNano())
	Guan_Yin := []int{}
	Guan_Yan := []int{}
	for i := 0; i < 10; i++ {
		Guan_Yin = append(Guan_Yin, rand.Intn(90)+10)
		Guan_Yan = append(Guan_Yan, rand.Intn(90)+10)
	}


	for len(Guan_Yin) > 0 && len(Guan_Yan) > 0 {
		for i := 0; i < len(Guan_Yin); i++ {
			for j := 0; j < len(Guan_Yan); j++ {
				if fight(&Guan_Yin[i], &Guan_Yan[j]) == "yin" {
					Guan_Yan = defeat(Guan_Yan, j)
					if j == len(Guan_Yan) {
						break
					}
				} else {
					Guan_Yin = defeat(Guan_Yin, i)
					if i == len(Guan_Yin) {
						break
					}
				}
			}
		}
	}


	fmt.Println(Guan_Yin)
	fmt.Println(Guan_Yan)
}