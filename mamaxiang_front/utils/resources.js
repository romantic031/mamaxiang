const Base_URL = "http://activate.navicat.com:8081"
export function getImage(name){
    return Base_URL+'/api/resources/image?name=' + name
}
export default{
	getImage
}