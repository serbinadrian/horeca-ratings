window.onload = () => {
    let labels = document.getElementsByClassName("auth-form-item");
    let inputs = document.getElementsByTagName('input');
    for (let i = 0; i < inputs.length; i++) {
        let index = i;
        inputs[i].addEventListener('input', () => {
            labels[index].classList.remove('errored')
        })
    }
}