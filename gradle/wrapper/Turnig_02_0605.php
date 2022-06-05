<?php

// error_reporting(0);
// error_reporting(error_reporting() & ~E_NOTICE);

class Solution {

     /**
     * @param String $s
     * @return Boolean 
     */
     function isValid($s) {
		 $stack = array();
		 $result = str_split($s);

		 for($Ux = 0 ; $Ux<  count($result) ; $Ux++ ){
			$Pointer = count($stack) - 1;
			$Data = $result[$Ux];

			// 堆棧是空的
			if ( $Pointer < 0 ){
				array_push($stack, $Data);
				print_r($stack);
				continue;
			}

			if ( $Data == ")" ){
				if ( $stack[$Pointer] == "(" ){
					array_pop($stack);
					print_r($stack);
					continue;
				} else {
					array_push($stack, $Data);
					print_r($stack);
					continue;
				}
			}

			if ( $Data == "}" ){
				if ( $stack[$Pointer] == "{" ){
					array_pop($stack);
					print_r($stack);
					continue;
				} else {
					array_push($stack, $Data);
					print_r($stack);
					continue;
				}
			}


			if ( $Data == "]" ){
				if ( $stack[$Pointer] == "[" ){
					array_pop($stack);
					continue;
				} else {
					array_push($stack, $Data);
					print_r($stack);
					continue;
				}
			}

			array_push($stack, $Data);
			print_r($stack);
		 }

		 //print_r($result);
		 if ( count($stack) == 0 ) {
			 return true;
		 } else {
			 return false;
		 }

	 }
}


// read inputs 
$s = readline();

// Solution
$solution = new Solution(); 
$output = $solution->isValid($s);

// print the output
echo $output ? 'valid' : 'invalid';

?>