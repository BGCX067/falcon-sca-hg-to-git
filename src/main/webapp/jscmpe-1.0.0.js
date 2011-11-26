/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function setMode(theForm, mode) {
    theForm.mode.value = mode;
    
    return true;
}

function editthis(theForm, target) {
    var targetMode = "edit" + target;
    setMode(theForm, targetMode)
    theForm.submit();
}

function savethis(theForm, target) {
    var targetMode = "save" + target;
    setMode(theForm, targetMode)
    theForm.submit();
}

function printThis(theForm) {
    setMode(theForm, "printFighter")
    theForm.submit();
}