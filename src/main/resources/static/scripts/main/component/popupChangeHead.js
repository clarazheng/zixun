(function (window) {
    var PopupChangeHead = Base.createClass('main.component.PopupChangeHead');
    var Popup = Base.getClass('main.component.Popup');
    var Upload = Base.getClass('main.component.Upload');
    var Component = Base.getClass('main.component.Component');
    var Util = Base.getClass('main.base.Util');

    Base.mix(PopupChangeHead, Component, {
        _tpl: [
            '<div>',
            '<div class="form-group">',
            '<div class="form-group">',
            '<label class="col-sm-2 control-label">上传图片</label>',
            '<div class="js-image-container col-sm-10">',
            '<a href="javascript:void(0);" class="btn btn-info btn-upload js-upload-btn" style="diplay:inline-block;position:relative;">上传图片</a>',
            '</div>',
            '</div>',
            '<div class="form-group">',
            '<div class="col-lg-10 col-lg-offset-2">',
            '<input type="submit" value="提交" class="js-submit btn btn-default btn-info">',
            '</div>',
            '</div>',
            '</div>'].join(''),
        listeners: [{
            name: 'render',
            type: 'custom',
            handler: function () {
                var that = this;
                var oEl = that.getEl();
                var oUploadBtn = oEl.find('a.js-upload-btn');
                new Upload({
                    targetEl: oUploadBtn,
                    url: '/addImage',
                    check: function (oFile, sType, nFileSize) {
                        var sMsg = nFileSize === 0 ? '文件大小不能为0' : /image/gi.test(sType || '') ? '' : '文件格式不正确';
                        sMsg && alert(sMsg);
                        return !sMsg;
                    },
                    call: function (oResult) {
                        var sUrl = $.trim(oResult.msg);
                        if (oResult.code !== 0) {
                            return alert('出现错误，请重试');
                        }
                        that.image = sUrl;
                        that.showImage(sUrl);
                    }
                });
            }
        }, {
            name: 'click input.js-submit',
            handler: function () {
                var that = this;
                var oEl = that.getEl();
                if (!that.image) {
                    return alert('图片不能为空');
                }
                if (that.requesting) {
                    return;
                }
                that.requesting = true;
                $.ajax({
                    url: '/changeHead',
                    method: 'post',
                    data: {image: that.image},                            //因为这部分里定义了image，title，link
                    dataType: 'json'
                }).done(function (oResult) {
                    that.emit('done');
                }).fail(function (oResult) {
                    alert('出现错误，请重试');
                }).always(function () {
                    that.requesting = false;
                });
            }
        }],
        show: fStaticShow
    }, {
        initialize: fInitialize,
        showImage: fShowImage
    });

    function fStaticShow(oConf) {
        var that = this;
        var oLogin = new PopupChangeHead(oConf);
        var oPopup = new Popup({
            title: '更改头像',
            width: 700,
            content: oLogin.html()
        });
        oLogin._popup = oPopup;
        Component.setEvents();
    }

    function fInitialize(oConf) {
        var that = this;
        delete oConf.renderTo;
        PopupChangeHead.superClass.initialize.apply(that, arguments);
    }

    function fShowImage(sUrl) {
        var that = this;
        var oEl = that.getEl();
        var sHtml = [
            '<div class="letter-pic-box">',
            '<a href="javascript:void(0);" class="icon-remove-circle"></a>',
            '<div class="mask"></div>',
            '<img src="' + sUrl + '">',
            '</div>'].join('');
        oEl.find('div.letter-pic-box').remove();
        oEl.find('div.js-image-container').prepend(sHtml);
    }

})(window);
