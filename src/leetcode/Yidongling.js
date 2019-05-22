/**
 * @param {number[]} nums
 * @return {void} Do not return anything, modify nums in-place instead.
 */
var moveZeroes = function(nums) {
	var n=nums.length;
    nums.forEach(function(e,i,a){
    	if(e==0){
    		[a[i],a[n]]=[a[n],a[i]];
    		n--;
    	}
    });
};

console.log(moveZeroes([1,0,3,4]));
