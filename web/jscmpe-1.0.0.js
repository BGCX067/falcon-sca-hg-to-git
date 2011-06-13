/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function setMode(theForm, mode) {
    theForm.mode.value = mode;
    
    return true;
}

function editthis(theForm, target) {
    setMode(theForm, target)
    theForm.submit();
}