import axios from 'axios';


class UIService{


    startAssesment(data){

       return axios.post('http://127.0.0.1:5000/start', 
       data)

    }

   
}

export default new UIService()