export default function randomArray(nums, arr) {
  if (arr.length <= nums) {
    return arr;
  }
  let res = [];
  let signs = {};
  while (nums > 0) {
    let ran = Math.floor(Math.random() * arr.length);
    if (!signs[ran]) {
      signs[ran] = true;
      res.push(arr[ran]);
      nums--;
    }
  }
  return res;
}
