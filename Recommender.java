package bookstuff;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Recommender {

	public static final int NUM_RECOMMENDATIONS = 3;

// finds similarity score of reviews between user and any of the 30 reviews(given index 0-29)
	
	public static double simScore(int A[], int B[][], int index) {
		int p1 = 0; int p2 = 0;
		double both = 0;
        for(int i = 0; i < 20; i++){
            if(A[i] != -1){
                p1 += Math.pow(A[i], 2);
            }
            if(B[index][i] != -1){
                p2 += Math.pow(B[index][i], 2);
            }
            if(A[i] != -1 && B[index][i] != -1){
                both += A[i]*B[index][i];
            }
        }
        
        double similarity = both/(p1*p2); //1/200
        return similarity;	
	}
	
// goes through all rating lists and returns an array containing similarity scores of the 30 reviews
	
	public static double[] simScoreList(int A[], int B[][]) {
		double similarityList[] = new double[30];
		for(int i = 0; i < 30; i++) {
			similarityList[i] = simScore(A, B, i);
			// testing System.out.print("similarity list = "  +similarityList[i] + ", ");
		}
		return similarityList;
	}
	
	public static double[] reviewAverage(double A[], int B[][]) {
		double bookAvg[] = new double[20];
		double ratingAvg[] = new double[20];
		double weightAvg = 0;
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 30; j++){
				if(B[j][i] != -1) {
					ratingAvg[i] += A[j]*B[j][i];
					weightAvg += A[j];
				}
			}	
		}
		for(int k = 0; k < 20; k++) {
			bookAvg[k] = ratingAvg[k]/weightAvg;
		}
		return bookAvg;
	}
		
	
// sorts and gets largest recommended values
	public static int[] sorting(double bkAvg[], int userRatingList[]){
		
	//removes values for books that have already been read so user can be recommended new books they have not yet read
		for(int i = 0; i < 20; i++) {
			if(userRatingList[i] != -1) {
				bkAvg[i] = 0;
			}
		}
		
	//keep original values of bkAvg as they will be sorted later and that array will change
		
		double bkAvgPlaceholder[] = new double[20];
		for(int i = 0; i < 20; i++) {
			bkAvgPlaceholder[i] = bkAvg[i];
		}
		
	//finding highest scores after sorting
		
		int largestIndeces[] = new int[NUM_RECOMMENDATIONS];
	    double temp[] = new double[NUM_RECOMMENDATIONS];
	    Arrays.sort(bkAvg);
	 
	    for(int i = 0; i < 3; i++){
	    //17 from length of useRating - NUM_RECOMMENDATIONS;
	    //temp now holds the last 3 indexes of bookAvg which when sorted are the largest numbers
	    	temp[i] = bkAvg[17 + i];
	    }
	
	//finding index of highest scores corresponding to title
	    
	    for(int i = 0; i < 3; i++){
	    	for(int j = 0; j < 20; j++){
	    		if(temp[i] == bkAvgPlaceholder[j]){
	    			largestIndeces[i] = j;
	                break;
	            }
	        }
	    }
	    return largestIndeces;
	}

	public static void results(double bkAvg[], int userRatingList[], String titles[]){

		int largestIndeces[] = sorting(bkAvg, userRatingList);
	    System.out.println("Based on your ratings, we recommend these books (selected from books you have not read) : ");
	    for(int i = 0; i < NUM_RECOMMENDATIONS-1; i++){
	    	System.out.print(titles[largestIndeces[i]] + ", ");
	    }
	    System.out.print("and " + titles[largestIndeces[NUM_RECOMMENDATIONS-1]]);

	}
	

    public static void main(String []args) throws FileNotFoundException{
    	
    // All scanners regarding files
    	
    	String titles[] = new String[20];
    	int ratings[][] = new int[30][20];

    	//put in file for all 20 book reviews from 30 people
    	Scanner ratingList = new Scanner(new File("C:\\Users\\Melody Lam\\Desktop\\ratings.txt"));
    
   

    		for(int i = 0; i<30 && ratingList.hasNextInt(); i++){
    			for(int j = 0; j<20 && ratingList.hasNextInt(); j++){
    				ratings[i][j] = ratingList.nextInt();
    			}
    		}

    	//put in file for title list
    	Scanner titleList = new Scanner(new File("C:\\Users\\Melody Lam\\Desktop\\titles.txt"));
    
    	while(titleList.hasNextLine()) {
    	
    		for(int i = 0; i < 20; i++) {
    			titles[i] = titleList.nextLine();
    		}
    
    	}
 
    //User input and store as data to use for comparison
    	
    	int userRatings[] = new int[20];
    	Scanner userInput = new Scanner(System.in);
    	System.out.println("You will be presented with an assortment of book titles, please rate them 1-5, or -1 if you have not read the book.");
    	
    	//note: error handling for 0, > 5, and < -1
    	for(int i = 0; i < 20; i++) {
    		System.out.println(titles[i]);
    		int userRating = userInput.nextInt();
    		if((userRating != -1) && (userRating < 1 || userRating > 5)) {
    			System.out.println("Invalid input, please input a rating 1-5, or -1 if you have not read the book.");
    			i--;
    			continue;
    		}
    		userRatings[i] = userRating;
    	}
    	
    	userInput.close();
    	
    
    double similarityList[] = new double[30];
    similarityList = simScoreList(userRatings, ratings);

   
	double bookAverage[] = new double[20];
	bookAverage = reviewAverage(similarityList, ratings);
	
	results(bookAverage, userRatings, titles);
    	
    	
    }
} 