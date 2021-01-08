import axios from 'axios';


class UIService{


    submitUrl(data){

       return axios.post('https://us-central1-naga-kopalle2.cloudfunctions.net/naga-faas1',{  headers : {
        'Content-Type': 'application/json'
    },
       data})

    }

    getFreImage(data){
        return axios.post('https://us-central1-naga-kopalle2.cloudfunctions.net/visualize_data',
        data,{  headers : {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'*'
        }})
 
     }
 


}

export default new UIService()