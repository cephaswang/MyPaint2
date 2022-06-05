<?php

class Solution {
	/*
	@param String[] $ops @return Integer
	*/

	function calPoints($ops) {

		if ( count($ops) > 1000 ){
			echo "Out of range 1000 ".count($ops) ;
			die;
		}

		$stack = array();

		for ($Ux = 0 ; $Ux < count($ops) ; $Ux++ ){
			$Data = trim($ops[$Ux]);

			if ( strlen($Data) < 1 ){
				continue;
			}

			echo "Data: $Data \r\n";
			$SC = count($stack) - 1;
			
			if (is_numeric($Data)){
				array_push($stack, intVal($Data) );
				print_r($stack);
				continue;
			}

			if ( $Data == "+" ){
				$A1 = $stack[$SC];
				$A2 = $stack[$SC-1];
				$Add5  = $A1 + $A2;
				array_push($stack, $Add5);
				print_r($stack);
				continue;
			}

			if ($Data == "D") {
				$Doub  = intVal($stack[$SC]) * 2;
				array_push($stack, $Doub);
				print_r($stack);
				continue;
			}

			if ($Data == "C") {
				array_pop($stack);
				print_r($stack);
				continue;
			}

		}

		return array_sum($stack);
	}
}

 
// read inputs
$ops = explode(' ', readline());

// Solution
$solution = new Solution();
$output = $solution->calPoints($ops);
// print the output
echo $output;
