pragma solidity 0.4.25;


contract RepositoryName {
    event Success(bool value);
    address public owner;
    address public delegator;
    string public repositoryName;
    mapping(address => bool) public authedAccounts;
    address[] public authedAccountList;
    uint256 public authedAccountSize = uint256(0);
    string public repositoryAddress;
    
    constructor(address _owner) public {
        owner = _owner == address(0) ? msg.sender : _owner;
        emit Success(true);
    }
    
    modifier hasAuthority() {
        require(msg.sender == owner || msg.sender == delegator);
        _;
    }
    
    function init(address _newOwner, string memory _repositoryName) public hasAuthority {
        require(_newOwner != address(0));
        owner = _newOwner;
        repositoryName = _repositoryName;
        emit Success(true);
    }
    
    function updateRepositoryName(string memory _repositoryName) public hasAuthority {
        repositoryName = _repositoryName;
        emit Success(true);
    }
    
    function updateRepositoryAddress(string memory _oldRepositoryAddress, string memory _newRepositoryAddress) public {
        require(msg.sender == owner || msg.sender == delegator || authedAccounts[msg.sender] == true);
        bytes memory nra = bytes(_newRepositoryAddress);
        require(nra.length > 1);
        bytes memory ora = bytes(_oldRepositoryAddress);
        bytes memory ra = bytes(repositoryAddress);
        // if length < 2 considered to null.
        require(ora.length < 2 && ra.length < 2 || keccak256(ora) == keccak256(ra));
        repositoryAddress = _newRepositoryAddress;
        emit Success(true);
    }
    
    function addTeamMember(address _member) public hasAuthority {
        require(_member != address(0) && authedAccounts[_member] != true);
        authedAccounts[_member] = true;
        authedAccountList.push(_member);
        authedAccountSize = authedAccountSize + 1;
        emit Success(true);
    }
    
    function removeTeamMember(address _member) public hasAuthority {
        require(_member != address(0) && authedAccounts[_member] == true);
        delete authedAccounts[_member];
        for (uint i=0; i<authedAccountList.length; i++) {
            if(authedAccountList[i] == _member){
                delete authedAccountList[i];
                break;
            }
        }
        authedAccountSize = authedAccountSize - 1;
        emit Success(true);
    }
    
    function changeOwner(address _newOwner) public hasAuthority {
        require(_newOwner != address(0));
        owner = _newOwner;
        emit Success(true);
    }
    
    function delegateTo(address _delegator) public hasAuthority {
        delegator = _delegator;
        emit Success(true);
    }
    
    function hasTeamMember(address _member) public view returns (bool) {
        return authedAccounts[_member];
    }
    
    function teamMemberAtIndex(uint256 _index) public view returns (address) {
        return authedAccountList[_index];
    }
}