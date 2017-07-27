/**
 var oPopupUpload = new PopupUpload({

});
 */
(function (window) {
    var PopupMsg = Base.createClass('main.component.PopupMsg');
    var Popup = Base.getClass('main.component.Popup');
    var Component = Base.getClass('main.component.Component');
    var Util = Base.getClass('main.base.Util');

    Base.mix(PopupMsg, Component, {
        _tpl: [
            '<div>',
            '<div class="form-group"><label class="col-sm-2 control-label">收信人</label><div class="col-sm-10"><input class="js-name form-control" type="text"></div></div>',
            '<div class="form-group"><label class="col-sm-2 control-label">内容</label><div class="col-sm-10"><input class="js-content form-control" type="text"></div></div>',
            '<div class="form-group">',
            '<div class="col-lg-10 col-lg-offset-2">',
            '<input type="submit" value="提交" class="js-submit btn btn-default btn-info">',
            '</div>',
            '</div>',
            '</div>'].join(''),
        listeners: [{
            name: 'click input.js-submit',
            handler: function () {
                var that = this;
                var oEl = that.getEl();
                var sName = $.trim(oEl.find('input.js-name').val());
                var sContent = $.trim(oEl.find('input.js-content').val());
                if (!sName) {
                    return alert('标题不能为空');
                }
                if (!sContent) {
                    return alert('链接不能为空');
                }
                if (that.requesting) {
                    return;
                }
                that.requesting = true;
                $.ajax({
                    url: '/msg/add',
                    method: 'post',
                    data: {toName: sName, content: sContent},                            //因为这部分里定义了image，title，link
                    dataType: 'json'
                }).done(function (oResult) {
                    that.emit('done');
                    window.location.href = '/msg/list';
                }).fail(function (oResult) {
                    alert('出现错误，请重试');
                }).always(function () {
                    that.requesting = false;
                });
            }
        }],
        show: fStaticShow
    }, {
        initialize: fInitialize
    });

    function fStaticShow(oConf) {
        var that = this;
        var oLogin = new PopupMsg(oConf);
        var oPopup = new Popup({
            title: '发信',
            width: 700,
            content: oLogin.html()
        });
        oLogin._popup = oPopup;
        Component.setEvents();
    }

    function fInitialize(oConf) {
        var that = this;
        delete oConf.renderTo;
        PopupMsg.superClass.initialize.apply(that, arguments);
    }

})(window);