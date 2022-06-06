<?php

class Solution {
	function sumofLeftLeaves(TreeNode $root = null) {
		$stack = array();
		print_r($root);
		if ( $root == null ){
			return 0;
		}

		while ( $root && $root->left != null ){
			$root = $root->left;
			echo $root->data."\r\n";
			// $sum += intVal( $root->data );
			array_push($stack, intVal($root->data) );
		}
		// echo $sum;
		return array_sum($stack);
	}
}

class TreeNode {
	public $data;
	public $left;
	public $right;

	public function __construct($data) {
		$this->data = $data;
	}
}

function deserialize(array $queue) {

	$nodes = array_map(function($val){
	if (is_null($val)) return null;
		return new TreeNode(intval ($val));
	}, $queue);

	$kids = array_reverse($nodes);
	$root = array_pop($kids);

	foreach($nodes as $node) {
		if($node) {
			$node->left  = array_pop ( $kids);
			$node->right = array_pop ( $kids);
		}
	}
	return $root;
}

function buildQueue($raw) {
	$queue = [];
	for($i = 0; $i < sizeof($raw); ++$i) {
		array_push($queue, $raw[$i] == 'null' ? null : intval($raw[$i]));
	}
	return $queue;
}

// $input = explode(',' , readline());
// $input = [9,3,20,15,7];
$input = [3,9,20,15,7];
$queue = buildQueue($input);
$tree  = deserialize($queue) ;
$solution = new Solution();
$output = $solution-> sumofLeftLeaves($tree) ;

echo "\r\n----[".$output."]----\r\n";
