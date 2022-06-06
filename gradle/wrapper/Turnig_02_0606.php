<?php

class Solution {
	function  twoSumLessThanK($A, $K) {
		print_r($A);

		arsort($A);

		$NA = array();
		foreach ($A as $key => $val) {
			array_push($NA, (int)$val );
		}

		print_r($NA);

		for ( $Ux = 0; $Ux< count($NA) ; $Ux++ ){
			if ($Ux > 0){
				$sum = (int)$NA[$Ux-1] + (int)$NA[$Ux];
				echo "$sum = ".$NA[$Ux-1]." + ".$NA[$Ux]." \r\n";
				if ((int)$sum > (int)$K){
					continue;
				} else {
					return $sum;
				}
			}
		}
		return -1;
	}
}

//$A = array_map( 'intval' , explode( ' ', readline() );
//$K = (int)readline();

$A = [10,20,30];
$K = 15;

$A = [34,23,1,24,75,33,54,8];
$K = 60;



// Solution
$solution = new Solution();
$output = $solution->twoSumLessThanK($A,$K);

echo $output;


?>